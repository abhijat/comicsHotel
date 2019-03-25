package com.abhijat.bareminimum.serialization

import com.abhijat.bareminimum.Query
import com.abhijat.bareminimum.RangeQuery
import com.abhijat.bareminimum.ScalarQuery
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class QueryDeserializer<T : Comparable<T>> : StdDeserializer<Query<T>>(Query::class.java) {

    private fun asDouble(node: JsonNode): T {
        return node.doubleValue() as T
    }

    private fun <T> asString(node: JsonNode): T {
        return node.textValue() as T
    }

    private fun <T> asBool(node: JsonNode): T {
        return node.booleanValue() as T
    }

    private fun extractScalarQuery(node: JsonNode): ScalarQuery<T> {
        val queryType = when (node["type"].textValue()) {
            "GreaterThanOrEqualTo" -> ScalarQuery.Type.GreaterThanOrEqualTo
            "LessThanOrEqualTo" -> ScalarQuery.Type.LessThanOrEqualTo
            "EqualTo" -> ScalarQuery.Type.EqualTo
            else -> throw Exception()
        }

        val againstNode = node["against"]

        return when (node["againstType"].textValue()) {
            "Number" -> ScalarQuery(queryType, asDouble(againstNode))
            "String" -> ScalarQuery(queryType, asString(againstNode))
            "Boolean" -> ScalarQuery(queryType, asBool(againstNode))
            else -> throw Exception()
        }
    }

    private fun extractRangeQuery(node: JsonNode): RangeQuery<T> {
        val queryType = when (node["type"].textValue()) {
            "WithinRange" -> RangeQuery.Type.WithinRange
            "OutsideRange" -> RangeQuery.Type.OutsideRange
            else -> throw Exception("unsupported query type ${node["type"].textValue()} for RangeQuery")
        }

        val againstNode = node["against"]

        val fromNode = againstNode["from"]
        val toNode = againstNode["to"]

        return when (node["againstType"].textValue()) {
            "Number" -> RangeQuery(queryType, asDouble(fromNode)..asDouble(toNode))
            else -> throw Exception()
        }
    }

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Query<T> {
        val parser = p ?: throw Exception()
        val node: JsonNode = parser.codec.readTree(p)

        val kind = node["kind"].textValue()

        return when (kind) {
            "scalar" -> {
                extractScalarQuery(node)
            }

            "range" -> {
                extractRangeQuery(node)
            }

            else -> throw Exception()
        }
    }
}
