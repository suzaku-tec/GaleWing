CREATE TABLE [feed_contents] ([uri] TEXT,
[value] TEXT,
[type] TEXT
);
CREATE TABLE [site] (
[uuid] TEXT NOT NULL,
[title] TEXT,
[htmlUrl] TEXT,
[xmlUrl] TEXT,
PRIMARY KEY(uuid)
);
CREATE TABLE [settings_class] (
[id] TEXT, --ID
[name] TEXT, --項目名
[parendId] TEXT, --親項目
PRIMARY KEY(id)
);
CREATE TABLE [setting] (
[classId] TEXT,
[id] TEXT,
[name] TEXT,
[setting] TEXT,
PRIMARY KEY(id)
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
