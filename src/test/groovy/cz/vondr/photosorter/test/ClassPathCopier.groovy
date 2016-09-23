package cz.vondr.photosorter.test
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class ClassPathCopier {

    /** Copy all files from package into specified targetDirectory */
    static void copyPackageIntoDirectory(String packageName, File targetDirectory) {
        packageName = cleanPackageName(packageName)
        targetDirectory.mkdirs()

        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/" + packageName + "/**")

        resources.each { resource ->
            Files.copy(resource.getInputStream(),
                    Paths.get(targetDirectory.absolutePath, resource.filename),
                    StandardCopyOption.REPLACE_EXISTING
            )
        }
    }

    private static String cleanPackageName(String packageName) {
        packageName = packageName.replace('.', '/')
        while (packageName.startsWith("/")) {
            packageName = packageName.substring(1)
        }
        while (packageName.endsWith("/")) {
            packageName = packageName.substring(0, packageName.size() - 1)
        }
        packageName
    }

}
