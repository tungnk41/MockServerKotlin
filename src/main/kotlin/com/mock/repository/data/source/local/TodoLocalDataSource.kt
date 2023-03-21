package com.mock.repository.data.source.local

import com.mock.repository.data.Todo
import com.mock.repository.data.source.TodoDataSource
import com.mock.repository.data.source.local.database.DatabaseFactory.query
import com.mock.repository.data.source.local.database.entities.TodoEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class TodoLocalDataSource : TodoDataSource {

    override suspend fun insert(userId: Long,todo: Todo): Todo? {
        var statement: InsertStatement<Number>? = null
        query {
            statement = TodoEntity.insert {
                it[user_id] = userId
                it[title] = todo.title
                it[content] = todo.content
                it[done] = todo.done
            }
        }
        return rowToTodo(statement?.resultedValues?.get(0))
    }

    override suspend fun update(userId: Long,todo: Todo): Int {
        val result = query {
            TodoEntity.update(where = { TodoEntity.user_id.eq(userId) and TodoEntity.id.eq(todo.id ?: -1)}) {
                it[title] = todo.title
                it[content] = todo.content
                it[done] = todo.done
            }
        }
        return result
    }

    override suspend fun findById(userId: Long,id: Long): Todo? {
        val result = query {
            TodoEntity
                .select { TodoEntity.user_id.eq(userId) and TodoEntity.id.eq(id) }
                .map { rowToTodo(it) }
                .singleOrNull()
        }

        return result
    }

    override suspend fun findByTitle(userId: Long,title: String): Todo? {
        val result = query {
            TodoEntity
                .select { TodoEntity.user_id.eq(userId) and TodoEntity.title.eq(title) }
                .map { rowToTodo(it) }
                .singleOrNull()
        }
        return result
    }

    override suspend fun find(userId: Long): List<Todo>? {
        val result = query {
            TodoEntity.select(where = {TodoEntity.user_id.eq(userId)}).mapNotNull { rowToTodo(it) }
        }
        return result
    }

    override suspend fun deleteById(userId: Long, id: Long): Int {
        val result = query {
            TodoEntity.deleteWhere {
                TodoEntity.user_id.eq(userId) and TodoEntity.id.eq(id)
            }
        }
        return result
    }

    private fun rowToTodo(row: ResultRow?): Todo? {
        if (row == null) {
            return null
        }
        return Todo(
            id = row[TodoEntity.id],
            title = row[TodoEntity.title],
            content = row[TodoEntity.content],
            done = row[TodoEntity.done]
        )
    }
}

