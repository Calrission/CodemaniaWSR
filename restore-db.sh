#!/bin/sh
echo "Waiting for the application to start..."
sleep 5

echo "Restoring database backups..."
for file in /app/backup-16.04.23/*.sql; do
    echo "Processing $file"
    PGPASSWORD="$POSTGRES_PASSWORD" psql -h "$codemania_db" -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f "$file"
done
echo "Database restoration completed!"
