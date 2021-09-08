@echo off
if not exist library.db (
  echo create DB
  sqlite3.exe Sample.db < ddl.sql
  echo create DB complete!!
) else (
  echo sqlite db exist
)

pause