# OS-Scheduler
OS Scheduling Algorithms is a project trying to simulate how a scheduler works. In this project, there are six different scheduling algorithms I have to implement. First come first serve, round robin, shortest process next, shortest remaining time, highest response ratio, feedback. According to the book, these algorithms can be separate into two category based on theirs decision mode. The first in category is the algorithms that use non-preemptive approach. In this case, once a job is in running state, it will continue to run until finish. The second is preemptive approach where a job can be stop and move back to the queue even if it not finish yet. First come first serve, shortest response ratio and highest response ratio use non-preemptive scheduling policy while round robin, shortest remaining time, feedback use preemptive. I followed the approach that was written in the project description.  First, I made a class to store all the jobs was given in the text file in. A job need to have name, arrive time, and the time it need to run to finish. Before implement the algorithm, I also had to create an abstract class contain all the elements shared between six algorithm. Finally I started to think how I should implement these algorithms.
