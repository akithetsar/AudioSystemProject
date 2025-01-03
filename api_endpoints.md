
## Endpoint definitions and operations

### **Collections:**

1. /cities
2. /users
3. /categories
4. /tracks
5. /packages
6. /subscriptions
7. /ratings
8. /favorites
9. /listenings

<hr>

#### **/cities**

```http
@GET /cities                // Returns all cities {Num. 18 in PDF}
@POST /cities               // Creates new city {Num. 1 in PDF}
```

#### **/users**

```http
@GET /users                 // Returns all users {Num. 19 in PDF}
@POST /users                // Creates new user {Num. 2 in PDF}
@PUT /users/{user_id}/email // Changes email address for a user {Num. 3 in PDF}
@PUT /users/{user_id}/city  // Changes city for a user {Num. 4 in PDF}
```

#### **/categories**

```http
@GET /categories            // Returns all categories {Num. 20 in PDF}
@POST /categories           // Creates new category {Num. 5 in PDF}
```

#### **/tracks**

```http
@GET /tracks                // Returns all audio tracks {Num. 21 in PDF}
@POST /tracks               // Creates new audio track {Num. 6 in PDF}
@PUT /tracks/{track_id}     // Changes name of an audio track {Num. 7 in PDF}
@POST /tracks/{track_id}/category  // Adds category to an audio track {Num. 8 in PDF}
```

#### **/packages**

```http
@GET /packages              // Returns all packages {Num. 23 in PDF}
@POST /packages             // Creates new package {Num. 9 in PDF}
@PUT /packages/{package_id} // Changes monthly price of a package {Num. 10 in PDF}
```

#### **/subscriptions**

```http
@POST /subscriptions        // Creates a new subscription for a user {Num. 11 in PDF}
@GET /subscriptions/{user_id} // Returns all subscriptions for a user {Num. 24 in PDF}
```

#### **/listenings**

```http
@POST /listenings           // Creates a new listening event for a user {Num. 12 in PDF}
@GET /listenings/{track_id} // Returns all listenings for a specific audio track {Num. 25 in PDF}
```

#### **/favorites**

```http
@POST /favorites            // Adds an audio track to the user's favorites list {Num. 13 in PDF}
@GET /favorites/{user_id}   // Returns the list of favorite audio tracks for a user {Num. 27 in PDF}
```

#### **/ratings**

```http
@POST /ratings              // Creates a rating for an audio track by a user {Num. 14 in PDF}
@PUT /ratings/{rating_id}   // Changes the rating for an audio track {Num. 15 in PDF}
@DELETE /ratings/{rating_id} // Deletes the rating for an audio track {Num. 16 in PDF}
@GET /ratings/{track_id}    // Returns all ratings for a specific audio track {Num. 26 in PDF}
```

#### **/tracks/{track_id}/categories**

```http
@GET /tracks/{track_id}/categories  // Returns the categories for a specific audio track {Num. 22 in PDF}
```

#### **/tracks/{track_id}/owner**

```http
@DELETE /tracks/{track_id}/owner  // Deletes the track if the user is the owner {Num. 17 in PDF}
```

