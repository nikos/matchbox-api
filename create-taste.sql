CREATE TABLE taste_preferences (
   user_id BIGINT NOT NULL,
   item_id BIGINT NOT NULL,
   preference FLOAT NOT NULL,
   PRIMARY KEY (user_id, item_id),
   INDEX (user_id),
   INDEX (item_id)
 )