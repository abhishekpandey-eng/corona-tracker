# corona-tracker
Get latest figures of corona virus 


Project setup requirements:
* JDK 1.8. (java development kit as project is build using java, to run we need JRE)
* Maven latest version: (for dependency management and build and run)


The project can be run as follows
* sh covid19_tracker.sh
* Application will run on port 8081, we can see the Dashboard by browsing "http://localhost:8081"
* Application will start pushing stats in every 6 hrs on "stats.queue"


Assumptions taken during creation of project:
* Supports stats only for one country, which can be set in application.yml.
* Shows all data in single page (assuming for small data set) in future we can give paginated response.
* Design for single server for now (saving latest Covid19 stats in memory after every 6 hours)
* Supports only single language English, we can also support multiple language to support Countries local language in future by adding internationalisation i18n.


Scalability (In future terms)
* Scalable from single-level to multi-level
* We can use Cache (like Redis) to save latest stats after every 6 hours to support distributed env as well as any locking mechanism
  so that only one scheduler will run at a time.