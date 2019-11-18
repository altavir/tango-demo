package scientifik.tango.sample

import fr.esrf.Tango.AttrWriteType
import fr.esrf.Tango.DevFailed
import org.tango.DeviceState
import org.tango.server.StateMachineBehavior
import org.tango.server.attribute.*

/**
 * A sample attribute
 *
 * @author ABEILLE
 */
class TestDynamicAttribute : IAttributeBehavior, ISetValueUpdater {
    private var readValue = 0.0
    private var writeValue = 100.0

    /**
     * Configure the attribute
     */
    @Throws(DevFailed::class)
    override fun getConfiguration(): AttributeConfiguration {
        val config =
            AttributeConfiguration()
        config.name = "testDynAttr"
        // attribute testDynAttr is a DevDouble


        config.type = Double::class.javaPrimitiveType
        // attribute testDynAttr is READ_WRITE


        config.writable = AttrWriteType.READ_WRITE
        // configure default attribute properties


        val properties = AttributePropertiesImpl()
        properties.label = "testDynAttr label"
        config.attributeProperties = properties
        return config
    }

    /**
     * Read the attribute
     */
    @Throws(DevFailed::class)
    override fun getValue(): AttributeValue {
        readValue = readValue + writeValue
        return AttributeValue(readValue)
    }

    /**
     * Write the attribute
     */
    @Throws(DevFailed::class)
    override fun setValue(value: AttributeValue) {
        writeValue = value.value as Double
    }

    /**
     * Configure state machine
     */
    @Throws(DevFailed::class)
    override fun getStateMachine(): StateMachineBehavior {
        val stateMachine = StateMachineBehavior()
        // denied states when writing this attribute


        stateMachine.setDeniedStates(DeviceState.FAULT, DeviceState.OFF)
        // the state after writing this attribute


        stateMachine.endState = DeviceState.FAULT
        return stateMachine
    }

    /**
     * Return last written value
     */
    @Throws(DevFailed::class)
    override fun getSetValue(): AttributeValue {
        return AttributeValue(writeValue)
    }
}