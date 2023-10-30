# Birb Service

[![vert.x](https://img.shields.io/badge/vert.x-4.4.6-purple.svg)](https://vertx.io)

This birb application was generated using http://start.vertx.io

-----------
A gRPC service that provides methods to get birb information.

## gRPC Methods

| Methods               | Description                                                      |
|-----------------------|------------------------------------------------------------------|
| `GetBirb`             | Retrieve detailed information about a bird by its unique ID.    |
| `GetBirbByType`       | Get a list of birds belonging to a specific bird type.           |

## Protobuf Schema

#### `Birb`

A message representing bird data, including its ID, name, sound, volume, and type.

| Field   | Type    | Description                          |
|---------|---------|--------------------------------------|
| `id`    | int64   | Unique identifier for the bird.      |
| `name`  | string  | The name of the bird.                |
| `sound` | string  | The sound the bird makes.           |
| `volume`| float   | The volume of the bird's sound.     |
| `type`  | BirbType| The type of the bird (e.g., Cockatiel, Lovebird, etc.).

#### `GetBirbRequest`

A message for specifying the ID of the bird you want to retrieve.

| Field   | Type    | Description                          |
|---------|---------|--------------------------------------|
| `id`    | int64   | Unique identifier for the bird you want to retrieve.

#### `GetBirbResponse`

A message containing the detailed information of the retrieved bird.

| Field   | Type    | Description                          |
|---------|---------|--------------------------------------|
| `birb`  | Birb    | The bird data that has been retrieved.

#### `GetBirbByTypeRequest`

A message for specifying the bird type to filter birds.

| Field   | Type    | Description                          |
|---------|---------|--------------------------------------|
| `type`  | BirbType| The type of bird for filtering.

#### `GetBirbByTypeResponse`

A message containing a list of birds of the specified type.

| Field   | Type    | Description                          |
|---------|---------|--------------------------------------|
| `birb`  | repeated Birb | A list of birds belonging to the specified type.


## Calling the gRPC endpoints

1. Using grpcurl.

```bash
grpcurl -plaintext -d '{"id": 1}' -import-path ./proto -proto birb.proto localhost:50051 birb.BirbService/GetBirb
```

```bash
grpcurl -plaintext -d '{"type": PIGEON}' -import-path ./proto -proto birb.proto localhost:50051 birb.BirbService/GetBirbByType
```

2. Using Postman (Not sure why it kept crashing on meeeee) `localhost:9000`, import Birb.proto and select GetBirb orGetBirbByType method.

#### GetBirbBy Message
```
{
    "id": "1"
}
```

#### Response
```
{
    "birb": {
        "id": "1",
        "name": "ah",
        "sound": "ahhhhh",
        "volume": 0.10000000149011612,
        "type": "PIGEON"
    }
}
```

#### GetBirbByType Message
```
{
    "type": "PIGEON"
}
```

#### Response
```
{
    "birb": [
        {
            "id": "1",
            "name": "ah",
            "sound": "ahhhhh",
            "volume": 0.10000000149011612,
            "type": "PIGEON"
        },
        {
            "id": "3",
            "name": "caca",
            "sound": "caaacaaaa",
            "volume": 0.30000001192092896,
            "type": "PIGEON"
        },
        {
            "id": "5",
            "name": "eheh",
            "sound": "eheheheh",
            "volume": 0.30000001192092896,
            "type": "PIGEON"
        }
    ]
}
```

## Getting Started

To build protobuf:
```
./gradlew generateProto
```

To launch your tests:
```
./gradlew clean test
```

To package your application:
```
./gradlew clean assemble
```

To run your application:
```
./gradlew clean run
```
