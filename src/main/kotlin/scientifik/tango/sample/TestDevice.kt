package scientifik.tango.sample

import fr.esrf.Tango.DevFailed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.tango.DeviceState
import org.tango.server.ServerManager
import org.tango.server.annotation.*
import org.tango.server.device.DeviceManager
import org.tango.server.dynamic.DynamicManager
import org.tango.server.testserver.JTangoTest
import scientifik.tango.sample.TestDevice.Companion.NO_DB_DEVICE_NAME
import scientifik.tango.sample.TestDevice.Companion.NO_DB_INSTANCE_NAME

@Device
class TestDevice {
    private val logger: Logger = LoggerFactory.getLogger(
        TestDevice::class.java
    )
    /**
     * A device property
     */
    @DeviceProperty(defaultValue = [""], description = "an example device property")
    var myProp: String? = null

    @ClassProperty(defaultValue = ["0"], description = "an example class property")
    var myClassProp = 0

    @DeviceProperties
    private var deviceProperties: Map<String, Array<String>>? = null

    /**
     * Attribute myAttribute READ WRITE, type DevDouble.
     */
    @Attribute
    @StateMachine(
        deniedStates = [DeviceState.FAULT],
        endState = DeviceState.DISABLE
    )
    var myAttribute = 0.0
        get() {
            logger.debug("getMyAttribute {}", field)
            return field
        }
        set(value) {
            logger.debug("setMyAttribute {}", value)
            field = value
        }

    /**
     * Manage dynamic attributes and commands
     */
    @DynamicManagement
    var dynamicManager: DynamicManager? = null

    /**
     * Manage state of the device
     */
    @State
    var state = DeviceState.OFF
    @DeviceManagement
    private var deviceManager: DeviceManager? = null

    /**
     * init device
     *
     * @throws DevFailed
     */
    @Init
    @StateMachine(endState = DeviceState.ON)
    @Throws(DevFailed::class)
    fun init() {
        logger.debug("myProp value = {}", myProp)
        logger.debug("myClassProp value = {}", myClassProp)
        logger.debug("deviceProperties value = {}", deviceProperties)
        // create a new dynamic attribute


        dynamicManager!!.addAttribute(TestDynamicAttribute())
        // create a new dynamic command


        dynamicManager!!.addCommand(TestDynamicCommand())
        logger.debug("init done for device {} ", deviceManager!!.name)
    }

    /**
     * delete device
     *
     * @throws DevFailed
     */
    @Delete
    @Throws(DevFailed::class)
    fun delete() {
        logger.debug("delete")
        // remove all dynamic commands and attributes


        dynamicManager!!.clearAll()
    }

    /**
     * Execute command start.
     */
    @Command
    @StateMachine(
        endState = DeviceState.RUNNING,
        deniedStates = [DeviceState.FAULT]
    )
    fun start() {
        logger.debug("start")
    }

    companion object {

        const val NO_DB_DEVICE_NAME = "1/1/1"
        const val NO_DB_GIOP_PORT = "12354"
        const val NO_DB_INSTANCE_NAME = "1"
        /**
         * Starts the server in nodb mode.
         *
         * @throws DevFailed
         */
        fun startNoDb() {
            System.setProperty("OAPort", NO_DB_GIOP_PORT)
            ServerManager.getInstance().start(
                arrayOf(
                    NO_DB_INSTANCE_NAME, "-nodb", "-dlist",
                    NO_DB_DEVICE_NAME
                ),
                TestDevice::class.java
            )
        }

        /**
         * Starts the server in nodb mode with a file for device and class
         * properties
         *
         * @throws DevFailed
         */
        @Throws(DevFailed::class)
        fun startNoDbFile() {
            System.setProperty("OAPort", NO_DB_GIOP_PORT)
            ServerManager.getInstance().start(
                arrayOf(
                    NO_DB_INSTANCE_NAME, "-nodb", "-dlist",
                    NO_DB_DEVICE_NAME,
                    "-file=" + JTangoTest::class.java.getResource("/noDbproperties.txt").path
                ), TestDevice::class.java
            )
        }
    }
}

/**
 * Starts the server.
 */
fun main() {
    ServerManager.getInstance().start(
        arrayOf(NO_DB_INSTANCE_NAME, "-nodb", "-dlist", NO_DB_DEVICE_NAME),
        TestDevice::class.java
    )
}