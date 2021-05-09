ALTER TABLE timetable_items ADD COLUMN "deleted" BOOLEAN;
ALTER TABLE timetable_items ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE timetable_items ALTER COLUMN deleted SET DEFAULT FALSE;