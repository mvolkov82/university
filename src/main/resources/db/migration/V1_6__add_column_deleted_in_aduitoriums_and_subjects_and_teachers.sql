ALTER TABLE auditoriums ADD COLUMN "deleted" BOOLEAN;
UPDATE auditoriums SET deleted = false;
ALTER TABLE auditoriums ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE auditoriums ALTER COLUMN deleted SET DEFAULT FALSE;

ALTER TABLE subjects ADD COLUMN "deleted" BOOLEAN;
UPDATE subjects SET deleted = false;
ALTER TABLE subjects ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE subjects ALTER COLUMN deleted SET DEFAULT FALSE;

ALTER TABLE teachers ADD COLUMN "deleted" BOOLEAN;
UPDATE teachers SET deleted = false;
ALTER TABLE teachers ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE teachers ALTER COLUMN deleted SET DEFAULT FALSE;