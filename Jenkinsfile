#!groovy

def runTest() {
    node {
        stage("checkout") {
            checkout scm
        }
        stage("test") {
            try {
                docker.build("java:8").inside {
                    sh "./gradlew clean test"
                }
            } finally {
                junit "**/test-results/*.xml"
            }
        }
    }
}

if (env.APPIUM_URL.contains("staging.testobject.org")) {
    echo "Testing on staging; locking resource"
    lock (resource: params.TESTOBJECT_DEVICE_ID) {
        runTest()
    }
} else {
    echo "Not testing on staging: ${env.APPIUM_URL}"
    try {
        runTest()
    } catch (err) {
        slackSend channel: "#${env.SLACK_CHANNEL}", color: "bad", message: "`${env.JOB_BASE_NAME}` failed: $err (<${BUILD_URL}|open>)", teamDomain: "${env.SLACK_SUBDOMAIN}", token: "${env.SLACK_TOKEN}"
        throw err
    }
}
