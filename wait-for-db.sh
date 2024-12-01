#!/bin/sh
echo "Waiting for the database to be ready..."
until pg_isready -h db -p 5432 -U value; do
    sleep 2
done
echo "Database is ready!"