plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "products"
include("soa-products")
include("soa-products-ejb")
