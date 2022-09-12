package com.harana.sdk.shared.utils

import shapeless.{HMapBuilder, Id, Poly1}
import shapeless.poly._

/**
  * Heterogeneous map with type-level key/value associations that are fixed by an arbitrary relation `R`.
  *
  * `HMap`s extend `Poly` and hence are also polymorphic function values with type-specific cases
  * corresponding to the map's type-level key/value associations.
  *
  * Note: keys and values are stored in erased form in an `HMap`.
  * Therefore one should be careful when using parameterized types as keys, because it might lead to unsoundness.
  * For a parameterized key type `K[T]` it is important that `T` is part of the `hashCode` / `equals` contract:
  *   - Good: `case class Key[T](id: T)` - `Key[Int](1)` and `Key[String]("1")` have different hash codes.
  *   - Bad: `case class Key[T](id: Int)` - `Key[Int](1)` and `Key[String](1)` have different types but the same hash code.
  */
class HMap[R[_, _]](underlying : Map[Any, Any] = Map.empty) extends Poly1 {
  def get[K, V](k : K)(implicit ev : R[K, V]) : Option[V] = underlying.get(k).asInstanceOf[Option[V]]
  def opt[K, V](k : K)(implicit ev : R[K, V]) : Option[V] = underlying.get(k).asInstanceOf[Option[V]]
  def apply[K, V](k: K)(implicit ev : R[K, V]): V = get[K, V](k).get

  def +[K, V](kv : (K, V))(implicit ev : R[K, V]) : HMap[R] = new HMap[R](underlying+kv)
  def -[K](k : K) : HMap[R] = new HMap[R](underlying-k)

  implicit def caseRel[K, V](implicit ev : R[K, V]) = Case1[this.type, K, V](get(_).get)
}
  
object HMap {
  def apply[R[_, _]] = new HMapBuilder[R]

  def empty[R[_, _]] = new HMap[R]
  def empty[R[_, _]](underlying : Map[Any, Any]) = new HMap[R](underlying)
}

/**
  * Type class witnessing the existence of a natural transformation between `K[_]` and `V[_]`.
  *
  * Use this trait to represent an `HMap` relation of the form `K[T]` maps to `V[T]`.
  *
  * @author Miles Sabin
  */
class ~?>[K[_], V[_]] extends Serializable {
  class λ[K, V] extends Serializable
}

object ~?> extends NatTRel0 {
  implicit def rel[K[_], V[_]] : K ~?> V = new (K ~?> V)

  implicit def idKeyWitness[V[_], T](implicit rel : Id ~?> V) : rel.λ[T, V[T]] = new rel.λ[Id[T], V[T]]
  implicit def idValueWitness[K[_], T](implicit rel : K ~?> Id) : rel.λ[K[T], T] = new rel.λ[K[T], Id[T]]
}

trait NatTRel0 {
  implicit def witness[K[_], V[_], T](implicit rel : K ~?> V) : rel.λ[K[T], V[T]] = new rel.λ[K[T], V[T]]
}
