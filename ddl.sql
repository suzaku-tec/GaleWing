CREATE TABLE [feed_contents] ([uri] TEXT,
[value] TEXT,
[type] TEXT
);
CREATE TABLE [feed] (
[title] TEXT,
[uuid] TEXT,
[link] TEXT,
[uri] TEXT,
[author] TEXT,
[comments] TEXT,
[publishedDate] TEXT,
[readed] BOOLEAN NOT NULL DEFAULT '0',
PRIMARY KEY(uuid,link)
);
CREATE TABLE [setting] (
[id] TEXT,
[setting] TEXT,
[overview] TEXT,
[description] TEXT,
PRIMARY KEY(id)
);
CREATE TABLE [site] (
[uuid] TEXT,
[title] TEXT,
[htmlUrl] TEXT,
[xmlUrl] TEXT,
[faviconBase64] TEXT,
PRIMARY KEY(uuid)
);
