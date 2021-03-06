package org.openbakery

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.TaskDependency
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.openbakery.appstore.AppstorePluginExtension
import org.openbakery.appstore.AppstoreUploadTask
import org.openbakery.appstore.AppstoreValidateTask
import org.openbakery.cpd.CpdTask
import org.openbakery.hockeykit.HockeyKitArchiveTask
import org.openbakery.hockeykit.HockeyKitImageTask
import org.openbakery.hockeykit.HockeyKitManifestTask
import org.openbakery.hockeykit.HockeyKitPluginExtension
import org.openbakery.oclint.OCLintPluginExtension
import org.openbakery.oclint.OCLintTask
import org.openbakery.signing.KeychainCreateTask
import org.openbakery.signing.ProvisioningInstallTask
import spock.lang.Specification

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.is

/**
 * Created by rene on 19.02.16.
 */
class XcodePluginSpecification extends Specification {

	Project project


	void setup() {
		project = ProjectBuilder.builder().build()
		project.apply plugin: org.openbakery.XcodePlugin
	}


	def "not contain unknown task"() {
		expect:
		project.tasks.findByName('unknown-task') == null
	}


	def "contain task archive"() {
		expect:
		project.tasks.findByName('archive') instanceof XcodeBuildArchiveTask
	}


	def "contain task clean"() {
		expect:
		project.tasks.findByName('xcodebuildClean') instanceof XcodeBuildCleanTask
	}


	def "contain task infoplist modify"() {
		expect:
		project.tasks.findByName('infoplistModify') instanceof InfoPlistModifyTask
	}


	def "contain task keychain create"() {
		expect:
		project.tasks.findByName('keychainCreate') instanceof KeychainCreateTask
	}


	def "contain task provisioning install"() {
		expect:
		project.tasks.findByName('provisioningInstall') instanceof ProvisioningInstallTask
	}


	def "contain task xcodebuild"() {
		expect:
		project.tasks.findByName('xcodebuild') instanceof XcodeBuildTask
	}


	def "appstoreUploadTask"() {
		expect:
		project.tasks.findByName('appstoreUpload') instanceof AppstoreUploadTask
	}



	def "appstoreValidateTask"() {
		expect:
		project.tasks.findByName('appstoreValidate') instanceof AppstoreValidateTask
	}



	def "contain task hockeykit"() {
		expect:
		project.tasks.findByName('hockeykit') instanceof DefaultTask
	}


	def "contain task hockeykit archive"() {
		expect:
		project.tasks.findByName('hockeykitArchive') instanceof HockeyKitArchiveTask
	}


	def "contain task hockeykit image"() {
		expect:
		project.tasks.findByName('hockeykitImage') instanceof HockeyKitImageTask
	}


	def "contain task hockeykit manifest"() {
		expect:
		project.tasks.findByName('hockeykitManifest') instanceof HockeyKitManifestTask
	}


	def "group tasks"() {
		project.tasks.each { task ->
			if (task.getClass().getName().startsWith("org.openbakery.XcodeBuildTask")) {
				assert task.group == XcodePlugin.XCODE_GROUP_NAME
			} else if (task.getClass().getName().startsWith("org.openbakery.Xcode")) {
				assert task.group == XcodePlugin.XCODE_GROUP_NAME
			} else if (task.getClass().getName().startsWith("org.openbakery.HockeyKit")) {
				assert task.group == XcodePlugin.HOCKEYKIT_GROUP_NAME
			}
			if (task.getClass().getName().startsWith("org.openbakery.HockeyApp")) {
				assert task.group == XcodePlugin.HOCKEYAPP_GROUP_NAME
			}
		}
	}


	def "contain extension xcodebuild"() {
		expect:
		project.extensions.findByName('xcodebuild') instanceof XcodeBuildPluginExtension
	}



	def "contain extension infoplist"() {
		expect:
		project.extensions.findByName('infoplist') instanceof InfoPlistExtension
	}


	def "contain extension hockeykit"() {
		expect:
		project.extensions.findByName('hockeykit') instanceof HockeyKitPluginExtension
	}


	def "contain extension appstore"() {
		expect:
		project.extensions.findByName('appstore') instanceof AppstorePluginExtension
	}



	def "contain extension oclint"() {
		expect:
		project.extensions.findByName('oclint') instanceof OCLintPluginExtension
	}


	def "oclint task"() {
		when:

		Task reportTask = project.tasks.findByName(XcodePlugin.OCLINT_REPORT_TASK_NAME)
		Task ocLintTask = project.tasks.findByName(XcodePlugin.OCLINT_TASK_NAME)
		TaskDependency dependency = reportTask.mustRunAfter

		then:
		reportTask instanceof OCLintTask
		ocLintTask instanceof Task
		ocLintTask.group == XcodePlugin.ANALYTICS_GROUP_NAME

		ocLintTask.dependsOn hasItem(project.getTasks().getByName(BasePlugin.CLEAN_TASK_NAME))
		ocLintTask.dependsOn hasItem(project.getTasks().getByName(XcodePlugin.XCODE_BUILD_TASK_NAME))
		ocLintTask.dependsOn hasItem(reportTask)

		dependency.getDependencies(reportTask) contains(project.getTasks().getByName(XcodePlugin.XCODE_BUILD_TASK_NAME))
	}

	def "cpd Tasks"() {
		when:
		CpdTask cpdTask = project.tasks.findByName(XcodePlugin.CPD_TASK_NAME)

		then:
		cpdTask instanceof CpdTask
		cpdTask.group == XcodePlugin.ANALYTICS_GROUP_NAME
	}

/*
	def "pass parameters to property"() {

		when:
		project.properties.put("xcodebuild.destination", "iPhone")


		then:
		project.xcodebuild.destinations contains("iPhone")

	}
	*/
}
