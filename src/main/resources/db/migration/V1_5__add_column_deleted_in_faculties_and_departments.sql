ALTER TABLE faculties ADD COLUMN "deleted" BOOLEAN;
UPDATE faculties SET deleted = false;
ALTER TABLE students ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE students ALTER COLUMN deleted SET DEFAULT FALSE;

ALTER TABLE departments ADD COLUMN "deleted" BOOLEAN;
UPDATE departments SET deleted = false;
ALTER TABLE departments ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE departments ALTER COLUMN deleted SET DEFAULT FALSE;