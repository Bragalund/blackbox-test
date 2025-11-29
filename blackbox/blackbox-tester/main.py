import os
import sys
import time
from typing import Sequence

import psycopg2
import requests

SWAGGER_URL = os.getenv("SWAGGER_URL", "http://swagger:8080/")
MAX_ATTEMPTS = int(os.getenv("SWAGGER_MAX_ATTEMPTS", "30"))
SLEEP_SECONDS = float(os.getenv("SWAGGER_POLL_INTERVAL", "2"))
EXPECTED_SNIPPETS = [
    snippet.strip()
    for snippet in os.getenv("SWAGGER_EXPECTED_SNIPPETS", "Swagger UI").split(",")
    if snippet.strip()
]
DB_USER = os.getenv("DB_USER", "postgres")
DB_PASSWORD = os.getenv("DB_PASSWORD", "mysecretpassword")
DB_NAME = os.getenv("DB_NAME", "postgres")
DB_PORT = int(os.getenv("DB_PORT", "5432"))
DB_HOST_LASTEBIL = os.getenv("DB_HOST_LASTEBIL", "postgres_lastebil_db")
DB_HOST_GRENS = os.getenv("DB_HOST_GRENS", "postgres_grensestasjoner_db")
LASTEBIL_TABLES = [("api", "lastebiler")]
GRENS_TABLES = [("api", "grensestasjoner")]


def log(message: str) -> None:
    print(message, flush=True)


def response_contains_html_content(response: requests.Response) -> bool:
    content_type = response.headers.get("Content-Type", "")
    if "text/html" in content_type:
        return True
    text = response.text.lower()
    return "<html" in text and "</html>" in text


def response_has_expected_content(response: requests.Response, snippets: Sequence[str]) -> tuple[bool, str | None]:
    text_lower = response.text.lower()
    for snippet in snippets:
        if snippet.lower() not in text_lower:
            return False, snippet
    return True, None


def wait_for_swagger() -> None:
    last_error: str | None = None
    for attempt in range(1, MAX_ATTEMPTS + 1):
        try:
            log(f"Attempt {attempt}/{MAX_ATTEMPTS}: requesting {SWAGGER_URL}")
            response = requests.get(SWAGGER_URL, timeout=10)
        except requests.RequestException as exc:  # pragma: no cover - runtime diagnostic
            last_error = str(exc)
            log(f"Request failed: {exc}")
        else:
            if response.status_code != 200:
                last_error = f"Unexpected HTTP status: {response.status_code}"
                log(last_error)
            elif not response_contains_html_content(response):
                last_error = "Response did not look like HTML"
                log(last_error)
            else:
                matches, missing_snippet = response_has_expected_content(response, EXPECTED_SNIPPETS)
                if matches:
                    log("Swagger UI responded with the expected HTML page. Tests passed.")
                    return
                last_error = f"Response missing expected snippet: {missing_snippet}"
                log(last_error)
        time.sleep(SLEEP_SECONDS)
    log("Swagger UI never reached the desired state.")
    if last_error:
        log(last_error)
    sys.exit(1)


def wait_for_postgres(host: str, expected_tables: list[tuple[str, str]]) -> None:
    last_error: str | None = None
    for attempt in range(1, MAX_ATTEMPTS + 1):
        try:
            log(f"Attempt {attempt}/{MAX_ATTEMPTS}: connecting to postgres at {host}:{DB_PORT}")
            with psycopg2.connect(
                host=host,
                port=DB_PORT,
                dbname=DB_NAME,
                user=DB_USER,
                password=DB_PASSWORD,
                connect_timeout=5,
            ) as conn:
                with conn.cursor() as cur:
                    missing: list[str] = []
                    for schema, table in expected_tables:
                        cur.execute(
                            """
                            select exists (
                                select 1
                                from information_schema.tables
                                where table_schema = %s and table_name = %s
                            );
                            """,
                            (schema, table),
                        )
                        exists = cur.fetchone()[0]
                        if not exists:
                            missing.append(f"{schema}.{table}")
                if not missing:
                    log(f"Postgres at {host} has all expected tables.")
                    return
                last_error = f"Missing tables on {host}: {', '.join(missing)}"
                log(last_error)
        except psycopg2.Error as exc:  # pragma: no cover - runtime diagnostic
            last_error = str(exc)
            log(f"Postgres check failed: {exc}")
        time.sleep(SLEEP_SECONDS)
    log(f"Postgres at {host} never reached the desired state.")
    if last_error:
        log(last_error)
    sys.exit(1)


if __name__ == "__main__":
    wait_for_postgres(DB_HOST_LASTEBIL, LASTEBIL_TABLES)
    wait_for_postgres(DB_HOST_GRENS, GRENS_TABLES)
    wait_for_swagger()
