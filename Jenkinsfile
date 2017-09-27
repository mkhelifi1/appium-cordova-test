#!groovy

def runTest() {
    node {
        stage("checkout") {
            checkout scm
        }
        stage("test") {
            try {
                docker.image("java:8").inside {
                    sh "./gradlew -w clean test"
                }
            } finally {
                junit "**/test-results/test/*.xml"
            }
        }
    }
}

if (env.APPIUM_URL.contains("staging.testobject.org")) {
    echo "Testing on staging; locking resource"
    lock (resource: params.TESTOBJECT_DEVICE) {
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
