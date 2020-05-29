/**
 * @format
 * @flow strict-local
 */
import * as React from 'react';
import {
  Dimensions,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  View,
  Text,
  StatusBar,
  Image
} from 'react-native';
import { Input, Button, Icon } from 'react-native-elements'
import {colors} from '../../../assets'
import { AppointmentForm} from '../../../components/Appointment/Form';
import { AppointmentCard } from '../../../components/Appointment/Card';
import {CustomModal} from '../../../components/CustomModal';

export interface IProps {
  navigation: any;
}
export interface IState {}

class AppointmentInfoScreen extends React.Component<IProps, IState> {
  state = {
    show: false
  }  
  showOverlay = () => {
    this.setState({ show: true}) 
  }
  hideOverlay = () => {
    this.setState({ show: false}) 
  }

  render() {
    return (
      <>  
        <CustomModal
          title="Update Appointment"
          children={<AppointmentForm/>} 
          visible={this.state.show} 
          onBackdropPress={this.hideOverlay} 
          overlayStyle={{ width: '90%', borderRadius: 5}} 
        />
      <View style={{ flex: 1 }}> 
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1}  />
        <SafeAreaView style={{ flex: 1, backgroundColor: colors.ocean5 }}>
          <ScrollView showsVerticalScrollIndicator={false}>
            <View style={styles.container}>
              <View style={{ flex: 1, flexDirection: 'row', justifyContent: 'flex-start', padding: 5}}>
                <Text style={styles.mainText}>Appointment</Text>
              </View>
              <View style={{ flex: 1, flexDirection: 'row', padding: 5}}>
                <View style={{ flex: 0.5, flexDirection: 'column' }}>
                  <Text style={styles.subText}>Name: </Text>
                </View>
                <View style={{ flex: 1, flexDirection: 'column' }}>
                  <Text style={styles.text}>Juan Dela Cruz</Text>
                </View>
              </View>
              <View style={{ flex: 1, flexDirection: 'row', padding: 5}}>
                <View style={{ flex: 0.5, flexDirection: 'column' }}>
                  <Text style={styles.subText}>Agenda: </Text>
                </View>
                <View style={{ flex: 1, flexDirection: 'column' }}>
                  <Text style={styles.text}>Check up with Mrs. Cruz</Text>
                </View>
              </View>
              <View style={{ flex: 1, flexDirection: 'row', padding: 5}}>
                <View style={{ flex: 0.5, flexDirection: 'column' }}>
                  <Text style={styles.subText}>Date: </Text>
                </View>
                <View style={{ flex: 1, flexDirection: 'column' }}>
                  <Text style={styles.text}>2020-01-01</Text>
                </View>
              </View>
              <View style={{ flex: 1, flexDirection: 'row', justifyContent: 'flex-start', padding: 5}}>
                <Text style={styles.subText}>Description: </Text>
              </View>
              <ScrollView showsVerticalScrollIndicator={true} style={{ height: 400, padding: 5 }}>
                <Text style={styles.description}> 
Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Gravida dictum fusce ut placerat orci. Consequat nisl vel pretium lectus quam. Massa tincidunt dui ut ornare lectus. Pharetra convallis posuere morbi leo. Fames ac turpis egestas integer. Tellus rutrum tellus pellentesque eu tincidunt tortor. Duis at consectetur lorem donec massa sapien faucibus et molestie. Sit amet dictum sit amet justo. Neque vitae tempus quam pellentesque nec nam aliquam.

Sed sed risus pretium quam vulputate dignissim suspendisse. Tristique senectus et netus et malesuada fames ac turpis egestas. Aliquet risus feugiat in ante metus dictum at tempor commodo. Nulla facilisi cras fermentum odio. Neque volutpat ac tincidunt vitae semper quis lectus nulla at. Nec sagittis aliquam malesuada bibendum arcu vitae. Velit ut tortor pretium viverra. Mattis molestie a iaculis at erat pellentesque adipiscing. Facilisi morbi tempus iaculis urna id. Sagittis orci a scelerisque purus semper eget duis at. Ac orci phasellus egestas tellus. Aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod. Faucibus et molestie ac feugiat sed.

Parturient montes nascetur ridiculus mus mauris vitae ultricies leo integer. In vitae turpis massa sed elementum tempus egestas sed sed. Ac felis donec et odio pellentesque diam. Fames ac turpis egestas sed tempus urna et pharetra. Imperdiet sed euismod nisi porta lorem mollis aliquam ut porttitor. Gravida neque convallis a cras semper auctor neque vitae tempus. Diam in arcu cursus euismod. Eget nullam non nisi est sit. Gravida quis blandit turpis cursus. Nibh nisl condimentum id venenatis a condimentum vitae. Pharetra vel turpis nunc eget lorem dolor sed viverra ipsum. Donec adipiscing tristique risus nec. Feugiat scelerisque varius morbi enim. Et tortor consequat id porta nibh venenatis cras sed felis. Rutrum tellus pellentesque eu tincidunt tortor. Turpis egestas maecenas pharetra convallis posuere.

Semper eget duis at tellus at urna. Cursus eget nunc scelerisque viverra mauris in aliquam sem fringilla. Porttitor rhoncus dolor purus non enim. Eu non diam phasellus vestibulum lorem sed risus ultricies tristique. Sit amet tellus cras adipiscing enim eu turpis egestas. Mauris a diam maecenas sed enim. Sit amet venenatis urna cursus eget nunc scelerisque viverra. Integer eget aliquet nibh praesent tristique magna sit amet. Mauris cursus mattis molestie a iaculis at erat. Eget nullam non nisi est sit amet facilisis. Netus et malesuada fames ac turpis egestas integer.

Nulla pharetra diam sit amet nisl. Ut lectus arcu bibendum at. Vitae suscipit tellus mauris a diam maecenas sed enim ut. Elementum eu facilisis sed odio. Facilisis sed odio morbi quis commodo odio. In iaculis nunc sed augue lacus viverra. Lacus sed turpis tincidunt id aliquet. Mauris vitae ultricies leo integer malesuada nunc vel. Tempor orci eu lobortis elementum nibh tellus molestie. Volutpat ac tincidunt vitae semper. Felis bibendum ut tristique et egestas quis ipsum. A pellentesque sit amet porttitor eget dolor morbi non arcu. Integer malesuada nunc vel risus commodo. Platea dictumst quisque sagittis purus sit. Nunc mattis enim ut tellus elementum sagittis. Varius vel pharetra vel turpis nunc eget. Non tellus orci ac auctor augue mauris augue neque. Urna condimentum mattis pellentesque id. Senectus et netus et malesuada.
                </Text>
              </ScrollView>
              <View style={{ flex: 1, flexDirection: 'row', paddingVertical: 20, paddingHorizontal: 5}}>
                <Button title="UPDATE" type="outline" buttonStyle={styles.buttonStyle} titleStyle={styles.titleStyle} onPress={this.showOverlay}/>
              </View>
            </View>
          </ScrollView>
        </SafeAreaView>
      </View>
        </>
    );
  } 
};
export default AppointmentInfoScreen;
const deviceWidth = Dimensions.get('window').width;
const deviceHeight = Dimensions.get('window').height;
const styles = StyleSheet.create({
  titleStyle: {
    color: colors.ocean1,
    fontFamily: 'Gill Sans',
    fontSize: 18
  },
  buttonStyle: {
    borderWidth: 0, 
  },
  container: {
    flex: 1,
    padding: 20, 
    flexDirection: 'column',
    backgroundColor: "#FFFFFF",
  },
  headerText: {
    fontSize: 30,
    textAlign: "center",
    margin: 10,
    color: 'white',
    fontWeight: "bold"
  },
  scrollViewVerticalStyle: {
    backgroundColor: colors.ocean5,
    flex: 1,
    flexDirection: 'column'
  },
  flex1: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'space-evenly',
    paddingHorizontal: 15,
    backgroundColor: colors.ocean5,
  },
  mainText: {
    fontFamily: 'Gill Sans',
    fontSize: 25,
    marginBottom: 10,
    fontWeight: '400',
    color: colors.black,
  },
  subText: {
    fontFamily: 'Gill Sans',
    fontSize: 16,
    color: colors.black,
  },
  text: {
    fontFamily: 'Roboto-Regular', 
    fontSize: 16, 
    color: colors.black,
    fontWeight: '300',
    marginHorizontal: 10,
  },
  description: {
    fontFamily: 'Roboto-Regular', 
    fontSize: 16,
    color: colors.black,
    fontWeight: '300',
    textAlign: 'justify',
  }
});

