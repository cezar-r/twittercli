# Twitter CLI CSC 345 Project

## Links
- [GitHub Repo](https://github.com/cezar-r/twittercli)
- [Presentation](https://www.youtube.com/watch?v=4Qp148yHDYk)

## Description
- This program implements a lightweight Twitter app that can be used on the command line. This app allows you to create an account, view tweets, search for friends, and write tweets yourself!
- The main goal behind this project was to implement various data structures and algorithms, that could support a program like Twitter. In this program, we implemented a **Hashtable**, **merge sort**, and **insertion sort** from scratch.
- The [hashtable](https://github.com/cezar-r/twittercli/blob/main/src/HashTable.java) was primarily used as the database for the app, as hashtables can store data in a relational manner. We needed a way to associate users with their tweets, followers, and password.
- The two sorting algorithms are used to sort the tweets on the users feed, based on a score given to those tweets. We implemented both, so that we can use [insertion sort](https://github.com/cezar-r/twittercli/blob/main/src/CoupleSorting.java#L32) when the dataset is smaller, and we can also use [merge sort](https://github.com/cezar-r/twittercli/blob/main/src/CoupleSorting.java#L53) when our dataset is larger.

## Installation and Running
#### You can compile and run the program with the commands below:
- Clone the repository
```
git clone https://github.com/cezar-r/twittercli
```
- Navigate into directory
```
cd twittercli/src
```
- Compile all java files
```
javac *.java
```
- Run the program
```
java Main
```
- Testing
```
java Test
```

## Basic Flow 

- The app will start in the main() call by initializing the `TwitterCLIViewerController`, where it will display the option to login/signup. The terminal output should look like something below. Would be cool to add some ascii art as well:

```
Welcome to Twitter CLI!

Login [1]
Signup [2]
Exit [Any key]
```
- Where the code underneath would look something like:

```java
public static void run() {
  option = input("Login[1]\n...");
  switch (opton):
    case 1 -> loginPage();
    case 2 -> signupPage();
}
```

- When the user logs in, it will check if the password matches with the one in the database. If the user signs up, it will create a new user in the database. 
- Once this is done, it will preload 50 tweets for the feed.

```java
public static List<Tweet> findRelevantTweets() {

  List<Tweet> tweets = new List<Tweet>();

  // get 3 tweets from each follower
  
  // find 5 most relevant unknowns
  // add 1 tweet from each relevant person
  // add 1 retweet where your follower retweeted a relevant unknown 
  
  // fill rest with random
  
  // shuffle it slightly
  
  return tweets;
}
```

- The home page should then display each tweet from this list of tweets, with options to go to the next tweet, previous tweet, retweet, as well as navigation to other screens:
```
----------------------------------------
@username

Tweet content goes here and we should 
try to format it so it wraps around like
this

4-4-2023 1:08 PM
----------------------------------------
Prev[1]        Next[2]        Retweet[3]

Home[#]  Search[s]  Tweet[d]  Profile[f]
```

- The search page will allow the user to type in a username, which will then display a profile page
```
----------------------------------------
Profile - @username

30 Tweets    20 Followers    24 Following

Follow[1]
View Tweets[2]
View Followers[3]
View Following[4]
----------------------------------------

Home[a]  Search[#]  Tweet[d]  Profile[f]
```

- The tweet page will let the user tweet 
```
Write a tweet (hit ENTER when done):


Home[a]  Search[s]  Tweet[#]  Profile[f]
```

- The profile page will display the profile page of the current user
```
----------------------------------------
Profile - @username

30 Tweets    20 Followers    24 Following

View Tweets[1]
View Followers[2]
View Following[3]
----------------------------------------

Home[a]  Search[s]  Tweet[d]  Profile[#]
```

## Eary Stage High Level UML Diagram
<img src = "Untitled.png">

