package utils

object ReflectUtils {

	def classForName[T](name: String): T = {
		Class.forName(name).newInstance().asInstanceOf[T]
	}
}