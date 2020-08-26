package com.barisic.covid_19manager.util

//CONNECTIONS
const val BASE_API_URL = "http://192.168.43.186:45455/"
const val JSON_TOKEN_URL = "https://login.microsoftonline.com/"

//FIELDS
const val OIB = "OIB"
const val ID = "ID"
const val MESSAGE = "MESSAGE"

const val HZJZ_NAME = "Hrvatski Zavod za Javno Zdravstvo"
const val HZJZ_PHONE = "+38598227753"

const val CLIENT_ID = "client_id"
const val SCOPE = "scope"
const val CLIENT_SECRET = "client_secret"
const val GRANT_TYPE = "grant_type"

const val LOGGED_USER = "logged_user"
const val LOGGED_USER_OIB = "logged_user_oib"
const val LOGGED_USER_ID = "logged_user_id"
const val LOGGED_USER_LAT = "logged_user_lat"
const val LOGGED_USER_LONG = "logged_user_long"
const val LOGGED_USER_EPIDEMIOLOGIST_ZZJZ = "logged_user_epidemiologist_name"
const val LOGGED_USER_EPIDEMIOLOGIST_NUMBER = "logged_user_epidemiologist_number"

//RESULTS
const val RESULT_SUCCESS = "SUCCESS"
const val RESULT_ERROR = "ERROR"
const val CREATED = 201
const val SERVICE_UNAVAILABLE = 503

//REQUEST_CODES
const val REQUEST_LOCATION_PERMISSION = 1
const val REQUEST_CALL = 385

//NOTIFICATIONS
const val ACTION_START_NOTIFICATION = "START_NOTIFICATION"

//ACTIONS
const val ACTION_LOCATION = "ACTION_LOCATION"
const val LOCATION_SERVICE_RUNNING = "location_service_running"

//LOCATION
const val LOCATION_SERVICE_TAG = "LOCATION_SERVICE"
const val NOTIFICATION_ID = 1224
const val KEY_REQUESTING_LOCATION_UPDATED = "requesting_location_update"

//EXCEPTIONS
const val TOKEN_ACCESS_FAILED = "TOKEN_ACCESS_FAILED"
const val PACIENT_ACCESS_FAILED = "PACIENT_ACCESS_FAILED"