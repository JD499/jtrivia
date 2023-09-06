# JTrivia API

JTrivia is a trivia API that provides trivia questions, categories, and other related data. Data is populated with the help of [jService API](http://jservice.io/).

## Getting Started

Clone the repository:

```bash
git clone https://github.com/JD499/jtrivia.git
```

Navigate to the project directory:

```bash
cd jtrivia
```

Install the required dependencies:

```bash
./gradlew build
```

Run the application:

```bash
./gradlew bootRun
```

## API Endpoints

### `/api/clues`
Fetch trivia clues based on various parameters.

**Parameters**:
- `value` (int): The value of the clue in dollars.
- `category` (int): The ID of the category you want to return.
- `min_date` (date): Earliest date to show, based on original air date.
- `max_date` (date): Latest date to show, based on original air date.
- `offset` (int): Offsets the returned clues. Useful for pagination.

### `/api/random`
Retrieve random trivia clues.

**Parameters**:
- `count` (int): Amount of clues to return. Limited to 100 at a time.

### `/api/final`
Fetch random final jeopardy questions. Note: All final jeopardy questions have a null value.

**Parameters**:
- `count` (int): Amount of clues to return. Limited to 100 at a time.

### `/api/categories`
Get a list of trivia categories.

**Parameters**:
- `count` (int): Amount of categories to return. Limited to 100 at a time.
- `offset` (int): Offsets the starting ID of categories returned. Useful for pagination.

### `/api/category`
Retrieve a specific category by its ID, along with its associated clues.

**Parameters**:
- `id` (int): The ID of the category to return. This parameter is required.

## Configuration
To configure the application for different environments, modify the `application.properties` file. Ensure sensitive data like database credentials are stored securely using environment variables or other secure methods.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
