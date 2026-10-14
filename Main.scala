import scala.util.Try

final case class RawRecord(idText: String, nameText: String, ageText: String, emailText: String)
final case class PersonRecord(id: Int, name: String, age: Int, email: String)

sealed trait ValidationError { def message: String }
final case class MissingField(field: String) extends ValidationError { def message = s"$field is required" }
final case class InvalidNumber(field: String, value: String) extends ValidationError {
  def message = s"$field must be a whole number (received '$value')"
}
final case class OutOfRange(field: String, value: Int, min: Int, max: Int) extends ValidationError {
  def message = s"$field must be between $min and $max (received $value)"
}
final case class InvalidEmail(value: String) extends ValidationError { def message = s"email '$value' is not valid" }

object DataValidator {
  private def clean(value: String): Option[String] =
    Option(value).map(_.trim).filter(_.nonEmpty)
  private def parseInt(value: String): Option[Int] = Try(value.toInt).toOption

  def validate(raw: RawRecord): Either[List[ValidationError], PersonRecord] = {
    val id = clean(raw.idText)
    val name = clean(raw.nameText)
    val ageText = clean(raw.ageText)
    val email = clean(raw.emailText)
    val idValue = id.flatMap(parseInt)
    val ageValue = ageText.flatMap(parseInt)
    var errors = List.empty[ValidationError]

    if (id.isEmpty) errors = errors :+ MissingField("id")
    else if (idValue.isEmpty) errors = errors :+ InvalidNumber("id", id.get)
    if (name.isEmpty) errors = errors :+ MissingField("name")
    if (ageText.isEmpty) errors = errors :+ MissingField("age")
    else if (ageValue.isEmpty) errors = errors :+ InvalidNumber("age", ageText.get)
    else if (ageValue.exists(a => a < 1 || a > 120))
      errors = errors :+ OutOfRange("age", ageValue.get, 1, 120)
    if (email.isEmpty) errors = errors :+ MissingField("email")
    else if (!email.get.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
      errors = errors :+ InvalidEmail(email.get)

    if (errors.nonEmpty) Left(errors)
    else Right(PersonRecord(idValue.get, name.get, ageValue.get, email.get))
  }
}

object Main {
  def describeResult(result: Either[List[ValidationError], PersonRecord]): String =
    result match {
      case Right(p) => s"VALID   | id=${p.id}, name=${p.name}, age=${p.age}, email=${p.email}"
      case Left(es) => "INVALID | " + es.map(_.message).mkString("; ")
    }

  def main(args: Array[String]): Unit = {
    val data = List(
      RawRecord("101", "Asha", "21", "asha@example.com"),
      RawRecord("102", "Rohan", "17", "rohan@example.com"),
      RawRecord("abc", "", "150", "wrong-email"),
      RawRecord("104", "Meera", "twenty", "meera@example.com"),
      RawRecord("", "Kabir", "25", "")
    )
    println("FUNCTIONAL DATA VALIDATION PIPELINE")
    println("-----------------------------------")
    data.foreach(row => println(describeResult(DataValidator.validate(row))))
    val results = data.map(DataValidator.validate)
    println("-----------------------------------")
    println(s"Summary: ${data.size} records checked, ${results.count(_.isRight)} valid, ${results.count(_.isLeft)} invalid.")
    println("Input records are immutable; validation returns results without changing them.")
  }
}
