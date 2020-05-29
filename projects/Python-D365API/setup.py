from setuptools import setup, find_packages

setup(
    name = "D365API",
    version = "0.1.0",
    packages = find_packages(),

    # Dependency
    install_requires = [
        "requests",
    ],

    # Metadata
    author = "Yan Kuang",
    author_email = "YTKme@Outlook.com",
    description = "Microsoft Dynamics 365 Application Programming Interface.",
    license = "GNU GENERAL PUBLIC LICENSE",
    keywords = "Microsoft Dynamics 365"
)