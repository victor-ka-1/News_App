# News_App

App, which shows news from https://newsapi.org/ for last 7 days.

Project, written in Kotlin using Paging 3. Downloaded news are caching in database. 

Used api requiers query parameter for requesting news. By default it set as "belarus", so there will be presented news about belarus. To change it, change const val QUERY_FOR_NEWS.

 Free version of this api is limited, and brcause of it, if there are a lot of news, you can't scroll down to 6th or 7th day because server does not send data and response with:
{"status":"error","code":"maximumResultsReached","message":"You have requested too many results. 
Developer accounts are limited to a max of 100 results. Please upgrade to a paid plan if you need more results."}

If you pull-to-refresh or click sync button, cache will be cleared and data will be downloading while you scrolling list down.

To see original article, click on source of news.

Provided searching in loaded news by title.


