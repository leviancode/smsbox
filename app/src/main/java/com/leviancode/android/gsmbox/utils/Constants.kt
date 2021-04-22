package com.leviancode.android.gsmbox.utils

const val DEFAULT_GROUP_COLOR = "#d59557"

const val DATABASE_NAME = "sms_box_db"
const val ARG_GROUP_ID = "group_id"
const val ARG_GROUP_NAME = "group_name"

const val RESULT_OK = "result_ok"
const val RESULT_CANCEL = "result_cancel"

const val REQ_SAVED = "request_saved"
const val REQ_SELECTED = "request_selected"
const val REQ_SELECT_RECIPIENT = "request_select_recipient"
const val REQ_SELECT_RECIPIENT_GROUP = "request_select_recipient_group"
const val REQ_CREATE_RECIPIENT_GROUP = "request_create_recipient_group"
const val REQ_MULTI_SELECT_RECIPIENT = "request_multi_select_recipient"
const val REQ_MULTI_SELECT_RECIPIENT_GROUP = "request_multi_select_recipient_group"
const val REQ_PICK_CONTACT = 1

const val RECIPIENT = "recipient"
const val RECIPIENT_GROUP = "recipient_group"
const val DATE_FORMAT = "yyy-MM-dd_HH:mm:ss"
const val PICK_ZIP_FILE = "pick_zip_file"

const val TAG_IN_GROUP = "in_group"

const val TEMPLATE_GROUPS_FILE_NAME = "template_groups.json"
const val TEMPLATES_FILE_NAME = "templates.json"
const val RECIPIENT_GROUPS_FILE_NAME = "recipient_groups.json"
const val RECIPIENTS_FILE_NAME = "recipients.json"
const val ZIP_DB_FILE_NAME = "gsmbox_backup_%s.zip"
val BACKUP_FILE_NAME
    get() = "smsbox_backup_${getFormatDate()}.sqlite"
val BACKUP_DIR
    get() = "backup_${getFormatDate()}"

const val FILE_PROVIDER_AUTH = "com.leviancode.android.gsmbox.fileprovider"