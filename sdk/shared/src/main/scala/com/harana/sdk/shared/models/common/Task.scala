package com.harana.sdk.shared.models.common

import java.time.Instant

import com.harana.sdk.shared.models.common.Entity.EntityId
import com.harana.sdk.shared.models.common.Task.TaskId
import com.harana.sdk.shared.models.common.User.UserId
import enumeratum._
import io.circe.generic.JsonCodec
import com.harana.sdk.shared.utils.CirceCodecs._
import com.harana.sdk.shared.utils.Random

@JsonCodec
case class Task(name: String,
                description: String,
                taskPriority: TaskPriority,
                taskState: TaskState,
                dueTime: Instant,
                assignedUsers: List[UserId],
                createdBy: Option[UserId],
                created: Instant,
                updatedBy: Option[UserId],
                updated: Instant,
                id: TaskId,
                status: Status,
                visibility: Visibility,
                version: Long,
                tags: Set[String],
                relationships: Map[String, EntityId])
    extends Entity with Serializable {

  type EntityType = Task
}

object Task {
  type TaskId = String

  def apply(name: String, description: String, priority: TaskPriority, state: TaskState, dueTime: Instant, assignedUsers: List[User], createdBy: Option[User], visibility: Visibility, tags: Set[String]): Task = {
    apply(name, description, priority, state, dueTime, assignedUsers.map(_.id), createdBy.map(_.id), Instant.now, createdBy.map(_.id), Instant.now, Random.long, Status.Active, visibility, 1L, tags, Map())
  }
}

sealed trait TaskPriority extends EnumEntry
case object TaskPriority extends Enum[TaskPriority] with CirceEnum[TaskPriority] {
  case object Highest extends TaskPriority
  case object High extends TaskPriority
  case object Normal extends TaskPriority
  case object Low extends TaskPriority
  case object Lowest extends TaskPriority
  val values = findValues
}

sealed trait TaskState extends EnumEntry
case object TaskState extends Enum[TaskState] with CirceEnum[TaskState] {
  case object Open extends TaskState
  case object OnHold extends TaskState
  case object Resolved extends TaskState
  case object Duplicate extends TaskState
  case object Invalid extends TaskState
  case object WontDo extends TaskState
  case object Closed extends TaskState
  val values = findValues
}
