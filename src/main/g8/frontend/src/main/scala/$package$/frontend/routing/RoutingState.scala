package $package$.frontend.routing

import io.udash._

sealed abstract class RoutingState(val parentState: Option[ContainerRoutingState]) extends State {
  override type HierarchyRoot = RoutingState
}

sealed abstract class ContainerRoutingState(parentState: Option[ContainerRoutingState])
  extends RoutingState(parentState)

sealed abstract class FinalRoutingState(parentState: Option[ContainerRoutingState])
  extends RoutingState(parentState)

case object RootState extends ContainerRoutingState(None)
case object LoginPageState extends FinalRoutingState(Some(RootState))
case object ChatState extends ContainerRoutingState(Some(RootState))
