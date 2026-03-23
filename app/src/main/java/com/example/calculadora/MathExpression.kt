package com.example.calculadora

class MathExpression(val tokens: MutableList<Token>) {
    val values: List<Token>
        get() = tokens

    val strValue: String
        get() = tokens
            .map { it.symbol }
            .joinToString(separator = " ") { it }

    constructor(token: Token) : this(mutableListOf()) {
        tokens.add(token)
    }

    constructor(number: Int) : this(NumberToken(number))

    fun append(token: Token) {
        when (token) {
            is NumberToken -> addNumberToken(token)
            is OperatorToken -> addOperatorToken(token)
            else -> tokens.add(token)
        }
    }

    private fun addNumberToken(token: NumberToken) {
        if (tokens.isEmpty()) {
            tokens.add(token)
            return
        }

        val last = tokens.last()
        if (last is NumberToken) {
            println("CU")
            tokens[tokens.lastIndex] = NumberToken((last.number * 10) + token.number)
        } else {
            tokens.add(token)
        }
    }

    private fun addOperatorToken(token: OperatorToken) {
        if (!tokens.isEmpty()) {
            val last = tokens.last()
            if (last is OperatorToken) {
                tokens[tokens.lastIndex] = token
                return
            }
        }

        tokens.add(token)
    }

    fun clear() = tokens.clear()

    fun calculate() {
        if (tokens.isEmpty()) return

        var accumulator = (tokens[0] as NumberToken).number
        var operator: OperatorToken? = null
        var current: Int?

        val iterator = tokens.iterator()
        iterator.next()

        while (iterator.hasNext()) {
            val token = iterator.next()

            if (operator != null) {
                if (token is OperatorToken) throw IllegalStateException("Invalid syntax")
                current = (token as NumberToken).number

                accumulator = operator.exec(accumulator, current)
                operator = null
            } else {
                if (token is NumberToken) throw IllegalStateException("Invalid syntax")
                operator = (token as OperatorToken)
                current = null
            }
        }

        tokens.clear()
        tokens.add(NumberToken(accumulator))
    }
}

data class NumberToken(val number: Int) : Token {
    override val symbol: String = number.toString()
}

enum class OperatorToken(override val symbol: String, val exec: (Int, Int) -> Int) : Token {
    SUM("+", { a, b -> a + b }),
    SUBTRACT("-", { a, b -> a - b }),
    DIVIDE("/", { a, b -> a / b }),
    MULTIPLY("*", { a, b -> a / b }),
    ;
}

interface Token {
    val symbol: String
}
