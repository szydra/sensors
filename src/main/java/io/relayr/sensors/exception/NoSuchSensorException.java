package io.relayr.sensors.exception;

/**
 * This exception is thrown if a sensor with specific id cannot be found.
 */
public class NoSuchSensorException extends RuntimeException {

    public NoSuchSensorException(String message) {
        super(message);
    }

}
