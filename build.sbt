name := "udash.g8"

test in Test := {
  val _ = (g8Test in Test).toTask("").value
}