package com.abhijat

enum class DomainObjectType {
    ComicBookCharacter,
}

fun extractObjectFromAggregate(aggregate: InputAggregate, domainObjectType: DomainObjectType): DomainType {
    return when (domainObjectType) {
        DomainObjectType.ComicBookCharacter -> aggregate.comicCharacter
    }
}
