# BestRoute
To find the shortest possible time for delivering order to various customers


Approach : 
•	Take no. of orders to be delivered.
•	Take input of different geo-locations for start point, customers and their respective restaurants.
•	With the help of haversine algorithm, calculate distance between 2 locations and create Adjacency matrix.
•	With this matrix and the source point, use Dijkstra algo to get the best possible route.
•	Then calculate distance using Adjacency matrix and route. 
•	Finally calculate time using the avg speed (given in problem statement).
