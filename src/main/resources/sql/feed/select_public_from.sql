select * from feed where TO_DATE(/*fromDate*/, 'YYYY-MM-DD HH24:MI:SS') < TO_DATE(publishedDate, 'YYYY-MM-DD HH24:MI:SS') and readed = false
