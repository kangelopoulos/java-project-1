# java-project-1

Our issues in this project were two fold. First, both of us our slightly more comfortable with other languages (JavaScript on one end, and C++ on the other). The Java syntax (especially for the JavaScript user) and use of a few classes caused us a bit of trouble trouble. 

At one point, we attempted to implement a ConcurrentHashMap class available through the Java API. We didn't realize this class is thread safe, meaning it has locks/etc implemented behind the scenes. Unfortunately this caused us some issues when trying to run the code - we kept seeing the same results for both runs with and without locks. Leaving that code in, alongside the manual lock we implemented, caused so much overhead that adding threads increased the amount of time the code took to run.
 
We also found that running the code in different environments gave shockingly different results. Running from VSCode and a terminal showed we achieved the maximum benefit (lowest run time) at 3 threads. However, running in an environment like eclipse only provided a run time benefit at 2 threads. 

The time calculation also proved a little tricky. At first, we were calculating the time after the I/O calls. This ended up showing very little benefit for adding additional threads. 