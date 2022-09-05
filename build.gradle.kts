import net.minecraftforge.gradle.userdev.tasks.RenameJarInPlace
import java.text.SimpleDateFormat
import java.util.*

buildscript {
	repositories {
		maven("https://files.minecraftforge.net/maven")
		maven("https://repo.spongepowered.org/maven")
		mavenCentral()
	}
	dependencies {
		classpath("net.minecraftforge.gradle:ForgeGradle:5.1.+") {
			isChanging = true
		}
		classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
	}
}

// Build script plugins
plugins {
	java
	id("net.minecraftforge.gradle")
	id("org.spongepowered.mixin")
	`maven-publish`
}

// --------------------------------------------------------------------------------

// Mod info
val modId = "flightcore"
val modGroup = "endorh.flightcore"
val githubRepo = "endorh/flightcore"
val modVersion = "0.5.3"
val mcVersion = "1.17.1"
val forge = "37.1.1"
val forgeVersion = "$mcVersion-$forge"
val mappingsChannel = "official"
val mappingsVersion = "1.17.1"
val mixinVersion = "0.8.5"
val minimalMixinVersion = "0.7.10"

group = modGroup
version = modVersion

val groupSlashed = modGroup.replace(".", "/")
val className = "FlightCore"
val modArtifactId = "$modId-$mcVersion"
val modMavenArtifact = "$modGroup:$modArtifactId:$modVersion"

// Attributes
val displayName = "Flight Core"
val vendor = "Endor H"
val credits = "xXJiazeXx"
val authors = "Endor H"
val issueTracker = ""
val page = ""
val updateJson = ""
val logoFile = "$modId.png"
val modDescription = """
	Generates events that allow mods to alter the way a player moves and rotates
	Also generates an event when the elytra item frame from an End Ship is generated,
	allowing mods to replace the elytra by other item
""".trimIndent()

// License
val license = "MIT"

val jarAttributes = mapOf(
	"TweakClass"                  to "org.spongepowered.asm.launch.MixinTweaker",
	"MixinConfigs"                to "mixins.$modId.json",
	"FMLCorePluginContainsFMLMod" to "true",
	"Specification-Title"         to modId,
	"Specification-Vendor"        to vendor,
	"Specification-Version"       to "1",
	"Implementation-Title"        to name,
	"Implementation-Version"      to "$version",
	"Implementation-Vendor"       to vendor,
	"Implementation-Timestamp"    to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
	"Maven-Artifact"              to modMavenArtifact
)

val modProperties = mapOf(
	"modid"         to modId,
	"display"       to displayName,
	"version"       to project.version,
	"mcversion"     to mcVersion,
	"mixinver"      to mixinVersion,
	"minmixin"      to minimalMixinVersion,
	"vendor"        to vendor,
	"authors"       to authors,
	"credits"       to credits,
	"license"       to license,
	"page"          to page,
	"issue_tracker" to issueTracker,
	"update_json"   to updateJson,
	"logo_file"     to logoFile,
	"description"   to modDescription,
	"group"         to modGroup,
	"class_name"    to className,
	"group_slashed" to groupSlashed
)

// Source Sets -----------------------------------------------------------------

sourceSets.main.get().resources {
	// Include resources generated by data generators.
	srcDir("src/generated/resources")
}

// Java options ----------------------------------------------------------------

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(16))
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

println(
	"Java: " + System.getProperty("java.version")
	+ " JVM: " + System.getProperty("java.vm.version") + "(" + System.getProperty("java.vendor")
	+ ") Arch: " + System.getProperty("os.arch"))

// Minecraft options -----------------------------------------------------------

minecraft {
	mappings(mappingsChannel, mappingsVersion)

	runs {
		val client = create("client") {
			workingDirectory(file("run"))
			
			property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
			property("forge.logging.console.level", "debug")
			
			jvmArg("-XX:+AllowEnhancedClassRedefinition")
			
			arg("-mixin.config=mixins.$modId.json")
			
			mods {
				create(modId) {
					source(sourceSets.main.get())
				}
			}
		}

		create("server") {
			workingDirectory(file("run"))
			
			property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
			property("forge.logging.console.level", "debug")
			
			jvmArg("-XX:+AllowEnhancedClassRedefinition")
			
			arg("-mixin.config=mixins.$modId.json")
			arg("nogui")

			mods {
				create(modId) {
					source(sourceSets.main.get())
				}
			}
		}
		
		create("client2") {
			parent(client)
			args("--username", "Dev2")
		}
	}
}

dependencies {
	"minecraft"("net.minecraftforge:forge:$forgeVersion")

	implementation("org.spongepowered:mixin:$mixinVersion")
	"annotationProcessor"("org.spongepowered:mixin:$mixinVersion:processor")
}

// Tasks --------------------------------------------------------------------------

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.classes {
	dependsOn(tasks.extractNatives.get())
}

lateinit var reobfJar: RenameJarInPlace
reobf {
	reobfJar = create("jar")
}

// Jar attributes
tasks.jar {
	archiveBaseName.set(modArtifactId)
	
	manifest {
		attributes(jarAttributes)
	}
	
	finalizedBy(reobfJar)
}

val sourcesJarTask = tasks.register<Jar>("sourcesJar") {
	group = "build"
	archiveBaseName.set(modArtifactId)
	archiveClassifier.set("sources")
	
	from(sourceSets.main.get().allJava)
	
	manifest {
		attributes(jarAttributes)
		attributes(mapOf("Maven-Artifact" to "$modMavenArtifact:${archiveClassifier.get()}"))
	}
}

val deobfJarTask = tasks.register<Jar>("deobfJar") {
	group = "build"
	archiveBaseName.set(modArtifactId)
	archiveClassifier.set("deobf")
	
	from(sourceSets.main.get().output)
	
	manifest {
		attributes(jarAttributes)
		attributes(mapOf("Maven-Artifact" to "$modMavenArtifact:deobf"))
	}
}

// Mixin refmap generation
mixin {
	add(sourceSets.main.get(), "mixins.$modId.refmap.json")
	config("mixins.$modId.json")
	
	// debug.verbose = true
	// debug.export = true
}

// Process resources
tasks.processResources {
	inputs.properties(modProperties)
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
	
	// Exclude development files
	exclude("**/.dev/**")
	
	from(sourceSets.main.get().resources.srcDirs) {
		// Expand properties in manifest files
		filesMatching(listOf("**/*.toml", "**/*.mcmeta")) {
			expand(modProperties)
		}
		// Expand properties in JSON resources except for translations
		filesMatching("**/*.json") {
			if (!path.contains("/lang/"))
				expand(modProperties)
		}
	}
}

val cleanBuildAssetsTask = tasks.register<Delete>("cleanBuildAssets") {
	delete("build/resources/main/assets")
}

// Make the clean task remove the run and logs folder
tasks.clean {
	delete("run")
	delete("logs")
	dependsOn(cleanBuildAssetsTask)
}

// Publishing ------------------------------------------------------------------

artifacts {
	archives(tasks.jar.get())
	archives(sourcesJarTask)
	archives(deobfJarTask)
}

publishing {
	repositories {
		maven("https://maven.pkg.github.com/$githubRepo") {
			name = "GitHubPackages"
			credentials {
				username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
				password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
			}
		}
		
		maven(rootProject.projectDir.parentFile.resolve("maven")) {
			name = "LocalMods"
		}
	}
	
	publications {
		register<MavenPublication>("mod") {
			artifactId = modArtifactId
			version = modVersion
			
			artifact(tasks.jar.get())
			artifact(sourcesJarTask)
			artifact(deobfJarTask)
			
			pom {
				name.set(displayName)
				url.set(page)
				description.set(modDescription)
			}
		}
	}
}
