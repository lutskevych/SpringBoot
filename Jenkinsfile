node {
    def mvnHome
    def jdk
    stage('Preparation') {
        git 'https://github.com/lutskevych/SpringBoot.git'
        mvnHome = tool 'M3'
        jdk = tool 'jdk1.8'
        env.JAVA_HOME = "${jdk}"
        env.PATH = "${mvnHome}/bin:${env.PATH}"
    }
    stage('Build') {
        if (isUnix()) {
            sh "mvn -Dmaven.test.failure.ignore clean package"
        } else {
            bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
        }
    }
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
        archive 'target/*.jar'
    }
}