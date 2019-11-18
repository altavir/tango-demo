package scientifik.tango.sample

import fr.esrf.Tango.DevFailed
import org.tango.server.StateMachineBehavior
import org.tango.server.command.CommandConfiguration
import org.tango.server.command.ICommandBehavior

class TestDynamicCommand : ICommandBehavior {
    @Throws(DevFailed::class)
    override fun getConfiguration(): CommandConfiguration {
        val config = CommandConfiguration()
        config.name = "testDynCmd"
        config.inType = Void.TYPE
        config.outType = Double::class.javaPrimitiveType
        return config
    }

    @Throws(DevFailed::class)
    override fun execute(arg: Any): Any {
        return 10.0
    }

    @Throws(DevFailed::class)
    override fun getStateMachine(): StateMachineBehavior? {
        return null
    }
}