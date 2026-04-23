ALTER TABLE inspections ALTER COLUMN overall_score TYPE NUMERIC(3,1);

UPDATE inspections 
SET overall_score = ROUND(CAST((frame_score + fork_score + brakes_score + drivetrain_score + wheels_score) / 5.0 AS NUMERIC), 1)
WHERE frame_score IS NOT NULL 
  AND fork_score IS NOT NULL 
  AND brakes_score IS NOT NULL 
  AND drivetrain_score IS NOT NULL 
  AND wheels_score IS NOT NULL;
