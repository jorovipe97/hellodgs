CREATE TABLE public.adsets (
    id SERIAL PRIMARY KEY,
--     Cammel case not done since all column names get lowercased on postgres
    advertiser_name varchar NULL,
    last_modified_by varchar NULL,
    properties JSON NULL
);

-- Insert dummy data.
INSERT INTO public.adsets(advertiser_name, last_modified_by, properties)
VALUES ('PlayOjo', 'The CICD Pipeline 1', '{"workbenchName": "PlayOjo", "bookmaker": "7797"}');

INSERT INTO public.adsets(advertiser_name, last_modified_by, properties)
VALUES ('Betly', 'The CICD Pipeline 2', '{"workbenchName": "Betly", "bookmaker": "1020"}');

INSERT INTO public.adsets(advertiser_name, last_modified_by, properties)
VALUES ('BetPlay', 'The CICD Pipeline 3', '{"workbenchName": "BetPlay", "bookmaker": "1036"}');

INSERT INTO public.adsets(advertiser_name, last_modified_by, properties)
VALUES ('Bet365', 'The CICD Pipeline 4', '{"workbenchName": "Bet365", "bookmaker": "5802"}');

-- Comment the table for description..
COMMENT ON TABLE public.adsets IS 'A table of adsets for testing gsm-adverts.';