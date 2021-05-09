UPDATE faculties SET deleted = false;
ALTER TABLE faculties ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE faculties ALTER COLUMN deleted SET DEFAULT FALSE;