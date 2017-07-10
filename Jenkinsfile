#!groovy

def runTest() {
    node {
        stage("staging test") {
            try {
                sh "./gradlew clean test"
            } finally {
                junit "**/test-results/*.xml"
            }
        }
    }
}

if (env.APPIUM_URL.contains("staging.testobject.org")) {
    lock (resource: params.TESTOBJECT_DEVICE_ID) {
        runTest()
    }
} else {
    try {
        runTest()
        if (env.SUCCESS_NOTIFICATION_ENABLED) {
            slackSend channel: "#${env.SLACK_CHANNEL}", color: "good", message: "`${env.JOB_BASE_NAME}` passed (<${BUILD_URL}|open>)", teamDomain: "${env.SLACK_SUBDOMAIN}", token: "${env.SLACK_TOKEN}"
        }
    } catch (err) {
        if (env.APPIUM_URL.contains("testobject.com") || env.FAILURE_NOTIFICATION_ENABLED) {
            slackSend channel: "#${env.SLACK_CHANNEL}", color: "bad", message: "`${env.JOB_BASE_NAME}` failed: $err (<${BUILD_URL}|open>)", teamDomain: "${env.SLACK_SUBDOMAIN}", token: "${env.SLACK_TOKEN}"
        }
        throw err
    }
}
