# -*- coding: utf-8 -*-


class Gun:
    def bang(self):
        print("빵야!")


class PoliceMan:
    def __init__(self, gun=None):
        self.__gun = gun

    def shoot(self):
        if self.__gun:
            self.__gun.bang()
        else:
            print("총이 없다..")


def main():
    revolver = Gun()
    police1 = PoliceMan(revolver)
    police1.shoot()

    police2 = PoliceMan()
    police2.shoot()


if __name__ == '__main__':
    main()
