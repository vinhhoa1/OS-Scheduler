import java.util.*;
import java.io.*;

import static java.lang.Math.*;
public class Scheduler {

    public Scheduler() {
        // TODO Auto-generated constructor stub
    }
    public static void main(String[] args)throws FileNotFoundException {
        ////To save user input
        int option = 0;
        int i =0;
        Job []job = new Job[5];
        /////////read file and store its information to an array
        File file = new File("jobs.txt");
        Scanner input = new Scanner(file);
        Scanner input2 = new Scanner(System.in);
        while(input.hasNext()){
            job[i] = new Job();
            job[i].name = input.next();
            job[i].arriveT = input.nextInt();
            job[i].duration = input.nextInt();
            i++;
        }
        /////////////////////////
        System.out.println("Pleaase enter from 1-6:\n1:First come first serve\n2:Round Robin\n3:Shortest process next\n4:Highest response ratio next\n5:Shortest remaining time\n6:Feedback");
        option = input2.nextInt();
            switch(option){
            case (1):////////////Run FCFS
                FCFS firstAl = new FCFS(job);
                firstAl.run();
                break;
            case (2):////////////Run Round Robin
                int quantum = 1;
            System.out.println("Enter quantum number: ");
                quantum = input2.nextInt();
                RR q2 = new RR(job,quantum);
                q2.run();
                break;
            case (3)://///////////Run SPN
                SPN ThirdAl = new SPN(job,1);
                ThirdAl.run();
                break;
            case (4):////////////Run HRRN
                HRRN FourthAl = new HRRN(job,1);
                FourthAl.run();
                break;
            case (5):///////////Run SRT
                SRT FifthAl = new SRT(job,1);
                FifthAl.run();
                break;
            case (6):////////////Run Feedback
                FB SixthAl = new FB(job,1);
                SixthAl.run();
                break;
          
             default:
                System.out.println("Invalid Input");
            
            }
            input.close();
       }
}

////////////Abstract class for all 6 different scheduling classes
abstract class Schedulers {
public  Job job1[];
public  int totaltime;
    Schedulers(){}
///////////////Find out how much time needed to run finish all the jobs
    Schedulers(Job [] a){
        job1 = a;
        for(int i=0 ;i< job1.length;i++){
            totaltime =totaltime + job1[i].duration;
        }  
    }
    abstract void run();
///////Display the graph
    void showgraph(){
        for(int i=0 ;i< job1.length;i++){
            System.out.println(job1[i].graph);
            job1[i].graph.setLength(0);
        }
    }
}

//////////////////Implement feedback scheduling
class FB extends Schedulers{
int quantum; ////////store quantum number
public FB (Job[] a, int quantum){
super(a);
this.quantum = quantum;
///////////////Find out how much time needed to run finish all the jobs
for(int i=0 ;i< job1.length;i++){
totaltime =totaltime + job1[i].duration;
}
}
void run(){
int i=0;//////////// running time
int u=0;
///////// initialize priority scheduling level
LinkedList<Integer> que1 = new LinkedList<Integer>();
LinkedList<Integer> que2= new LinkedList<Integer>();
LinkedList<Integer> que3 = new LinkedList<Integer>();
    while(i<=totaltime){/////////
        ///////////////////add a job to queue at its arrive time
        for(int j=0;j<job1.length;j++){
            if(job1[j].arriveT == i){
                if(job1[j].duration!=0)
                que1.addFirst(j);////////Add to queue
            }
        }
        ////////////////
        if(que1.size()!=0){///////////Run first queue level
            u = que1.poll();
            for(int y=0;y<job1.length;y++){
                if(u!=y){
                    job1[y].runNonP(0, quantum);
                }
               }
               job1[u].runNonP(quantum,0);
               if(job1[u].duration!=0){
                   que2.add(u);
                   
               }
            
        }
        else if(que2.size()!=0){///////////////If first level empty run second level
            u = que2.poll();
            for(int y=0;y<job1.length;y++){
                if(u!=y){
                    job1[y].runNonP(0, quantum);
                }
               }
               job1[u].runNonP(quantum,0);
               if(job1[u].duration!=0){
                   if(que1.size() == 0 && que2.size() == 0  ){
                       que2.add(u);
                   }
                   else{
                   que3.add(u);
                   }
               }
        }
        else if(que3.size()!=0){////////////////////If second level empty, run third level
            u = que3.poll();
            for(int y=0;y<job1.length;y++){
                if(u!=y){
                    job1[y].runNonP(0, quantum);
                }
               }
               job1[u].runNonP(quantum,0);
               if(job1[u].duration!=0)
               que3.add(u);
        }

i++;////////// increase time
}
showgraph();
}
}
//////////////////
class SRT extends Schedulers{
int quantum;
public SRT (Job[] a, int quantum){
    super(a);
    this.quantum = quantum;
///////////////Find out how much time needed to run finish all the jobs
    for(int i=0 ;i< job1.length;i++){
        totaltime =totaltime + job1[i].duration;
    }
}
void run(){
    int i=0;//////////store running time
    int u=0;
        /////////////////////////list of jobs need to run
        LinkedList<Integer> list = new LinkedList<Integer>();
        /////////////////Running queue
        LinkedList<Integer> que = new LinkedList<Integer>();
        ////////////////////////////list to store job's remaining time to finish
        LinkedList<Integer> dur = new LinkedList<Integer>();
        //Listing job by order by arrived time
            for(int j=0;j<job1.length;j++){
                    list.add(j);
            }
    while(i<=totaltime+10){
        ///////////////////////add jobs to queue after they arrive (only job that haven't finish yet
        for(int j=0;j<job1.length;j++){
            if(job1[j].arriveT <= i){
                if(list.contains(j)){
                    que.add(j);
                    dur.add(job1[j].duration);
                }
            }
        }
            loop1:
               while(que.size()!=0&&dur.size()!=0){ ///////////dont run if there is nothing in queue
                   //////////////find out jobs with minium remaining time 
                   u =Collections.min(dur); 
                   //////////////remove from list to run
                    dur.removeFirstOccurrence(u);
                    ////////////////start runnning job
                    for(int j=0;j<job1.length;j++){
                        if(job1[j].duration!=0){
                        if(job1[j].duration == u){ ///////////search for job by its remaning time
                            u = j;
                            que.removeFirstOccurrence(u);///////////////remove job from running queue
                            if(job1[u].duration==0){
                            list.removeFirstOccurrence(u); ///////////////remove job from  list of job that haven't finish
                            }
                            for(int y=0;y<job1.length;y++){
                                if(u!=y){
                                    job1[y].runNonP(0, quantum);//////////add space to job that are not running
                                }
                               }
                               job1[u].runNonP(quantum,0);////////////display name of the running job
                            break loop1;
                        }
                        }
                    }
                }
        i++;
    }
showgraph();
}
}
/////////////////
class HRRN extends Schedulers{
int quantum;
public HRRN (Job[] a, int quantum){
    super(a);
    this.quantum = quantum;
///////////////Find out how much time needed to run finish all the jobs
    for(int i=0 ;i< job1.length;i++){
        totaltime =totaltime + job1[i].duration;
    }
}
void run(){
     int i=0;////////////////running time
     int u=0;
        /////////////////////////list of jobs need to run
        LinkedList<Integer> list = new LinkedList<Integer>();
        /////////////////Running queue
        LinkedList<Integer> que = new LinkedList<Integer>();
        ////////////////////////////list to store job's response ratio
        LinkedList<Integer> dur = new LinkedList<Integer>();
        //Listing job by order by arrived time
        for(int j=0;j<job1.length;j++){
                    list.add(j);
         }
    while(i<=totaltime+10){
        for(int j=0;j<job1.length;j++){
            ///////////////Add job and its response ratio if it arrive
            if(job1[j].arriveT <= i){
                if(list.contains(j)){
                    que.add(j);
                    dur.add(((i-job1[j].arriveT)+job1[j].duration)/job1[j].duration);
                }
            }
        }
            loop1:
               while(que.size()!=0&&dur.size()!=0){
                   //////////////////get highest response ratio from list
                    u =Collections.max(dur);
                    dur.removeFirstOccurrence(u);
                    for(int j=0;j<job1.length;j++){
                        if(job1[j].duration!=0){
                            ////////////////////Pick a job with highest response ratio and run
                        if(((i-job1[j].arriveT)+job1[j].duration)/job1[j].duration == u){
                            u = j;
                            que.removeFirstOccurrence(u);
                            list.removeFirstOccurrence(u);
                            for(int y=0;y<job1.length;y++){
                                if(u!=y){
                                    job1[y].runNonP(0, job1[u].duration);/////////Add space to job on graph that are not running
                                }
                               }
                                i= i+ job1[u].duration-1;
                               job1[u].runNonP(job1[u].duration,0);/////////////Add running job name to graph
                               
                            break loop1;
                        }
                        }
                    }
 
                }
        i++;
    }
showgraph();
}
}
//////////////
class SPN extends Schedulers{
int quantum;
public SPN (Job[] a, int quantum){
    
    super(a);
    this.quantum = quantum;
///////////////Find out how much time needed to run finish all the jobs
    for(int i=0 ;i< job1.length;i++){
        totaltime =totaltime + job1[i].duration;
    }
}
void run(){
    int i=0;
    int u=0;
/////////////////////////list of jobs need to run
LinkedList<Integer> list = new LinkedList<Integer>();
/////////////////Running queue
LinkedList<Integer> que = new LinkedList<Integer>();
////////////////////////////list to store job's remaining tien ro finish
LinkedList<Integer> dur = new LinkedList<Integer>();
        //Listing job by order by arrived time
      
            for(int j=0;j<job1.length;j++){
                    list.add(j);
            }
    while(i<=totaltime+10){
        //////////////////add job and it's remaining time to the queue
        for(int j=0;j<job1.length;j++){
            if(job1[j].arriveT <= i){
                if(list.contains(j)){
                    que.add(j);
                    dur.add(job1[j].duration);
                }
            }
        }
            loop1:
               while(que.size()!=0&&dur.size()!=0){/////////////Run if the queue is not empty
                    u =Collections.min(dur);
                    dur.removeFirstOccurrence(u);
                    for(int j=0;j<job1.length;j++){
                        if(job1[j].duration == u){//////////find job with the short remaning time and remove it from lists
                            u = j;
                            que.removeFirstOccurrence(u);
                            list.removeFirstOccurrence(u);
                            for(int y=0;y<job1.length;y++){
                                if(u!=y){
                                    job1[y].runNonP(0, job1[u].duration);/////////add space to graph
                                }
                               }
                                i= i+ job1[u].duration-1;
                               job1[u].runNonP(job1[u].duration,0);//////////add job name to graph
                               
                            break loop1;
                        }
                    }
 
                }
        i++;
    }
showgraph();
}
}
//////////////


class RR extends Schedulers{
    int quantum;
    public RR (Job[] a, int quantum){
        
        super(a);
        this.quantum = quantum;
///////////////Find out how much time needed to run finish all the jobs
        for(int i=0 ;i< job1.length;i++){
            totaltime =totaltime + job1[i].duration;
        }
    }
    void run(){
        int mode =0;
        /////////////////queue list
        LinkedList<Integer> que = new LinkedList<Integer>();
        //Listing job by order by arrived time
        int[]arriveOrder = new int[job1.length];
        int counter=0;
        /////////////sort jobs by it arrive time
        for(int h=0 ;h< totaltime;h++){
            for(int j=0;j<job1.length;j++){
                if(job1[j].arriveT == h){
                    arriveOrder[counter] = j;
                    counter++;
                }
            }
        }
    //////////////
        int i=0;
        int u=-1;
        int temp = 0;
        while(i<=totaltime){
            ///////////////Add job to running queue when it arrive
            for(int j=0;j<job1.length;j++){
                if(job1[arriveOrder[j]].arriveT <= i){
                    if(!que.contains(arriveOrder[j]) && j!=u ){
                        que.add(arriveOrder[j]);
                    }
                }
            }
            if(u!= -1){/////////To add job back to the queue after it ran
                que.add(u);
            }
                u =  que.poll();///////////get the first job on the list and run
                /////////////Run in two mode, make sure the job will stop running if it remaining time is less than quantum number
                    if( job1[arriveOrder[u]].duration!= 0 ){
                        if(quantum> job1[arriveOrder[u]].duration){
                            temp = job1[arriveOrder[u]].duration;
                            job1[arriveOrder[u]].runP(job1[arriveOrder[u]].duration,0);//////////////add job name to graph
                            mode = 1;
                        }
                        else{
                            mode = 0;
                        job1[arriveOrder[u]].runP(quantum,0);//////////////add job name to graph
                        }
                    if(mode == 1){/////////////////////////Mode 1 when job remaining time is less than quantum
                                    for(int h=0 ;h< job1.length;h++){
                                        if(h != arriveOrder[u]){
                                            job1[arriveOrder[h]].runP(0,temp);///////Add space to graph
                                        }
                                    }
                    }
                    else{//////////////////////Mode 0 when job remaining time is bigger than quantum number
                                    for(int h=0 ;h< job1.length;h++){
                                        if(h != arriveOrder[u]){
                                        job1[arriveOrder[h]].runP(0,quantum);///////Add space to graph
                                        }
                                    }   
                    }
                    }
                    if(mode ==1){
                        i= i+temp;
                    }
                    else{
                        i=i+quantum;
                    }
        }
    showgraph();
    }
}

class FCFS extends Schedulers{
public FCFS (Job[] a){
   super(a);
}
    void run(){
        int i=0;///running time
        while(i<=totaltime){
            for(int j=0;j<job1.length;j++){
                if(job1[j].arriveT == i){//////////run job when they arrive
                    for(int k=0;k<job1.length;k++){
                        if(k!=j){////////only add space to job that are not running
                            job1[k].runNonP(0, job1[j].duration);//////add space to graph
                        }
                    }
                    job1[j].runNonP(job1[j].duration,0); //////////add job name to graph
                }
            }
            i++;
        }
    showgraph();
    }
}
    class Job {
    public String name = null;/////////job name
    public int arriveT = 0;///////job's arrive time
    public int duration = 0;///////job's remaining time to finish
    StringBuilder graph = new StringBuilder(22);///////////store graph info
    Job(){/////////constructor
        String name = null;
        arriveT = 0;
        duration = 0;
    };

    Job(String name, int a, int d){//////constructor with parameter
        this.name = name;
        this.arriveT = a;
        this.duration =d;
        graph = new StringBuilder(22);
    }
    public void runNonP(int runtime, int wt){//////////run non pre-emptively, until job is finish
        while(runtime != 0&& duration!=0){
            graph.append(name);
            duration--;
            runtime--;
        }
        while(wt !=0){
            graph.append(' ');
            wt--;
        }
    }
    public void runP(int time, int wt){/////////////run pre-emptibely, run until the scheduler decide to stop the job
        int time2 = time;
        
        while(time2 != 0 && duration!=0){
            graph.append(this.name);
            time2--;
            duration--;
        }
        while(wt !=0){
            graph.append(' ');
            wt--;
        }
    }
}

