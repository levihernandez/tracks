from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import os
import time

# Add retry logic for database connection
def get_db_url():
    return os.getenv("DATABASE_URL", "postgresql://routes_user:routes_password@routes-db:5432/routes_db")

def create_db_engine(max_retries=5, retry_delay=5):
    retries = 0
    while retries < max_retries:
        try:
            engine = create_engine(get_db_url())
            engine.connect()
            return engine
        except Exception as e:
            retries += 1
            if retries == max_retries:
                raise e
            time.sleep(retry_delay)

engine = create_db_engine()
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()