package com.example.dlworkhelper.dataclasses

data class TurnoverResponse(val count: Int,
                            val items: List<TurnoverNote>,
                            val total: Float)
