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
import { Bubble, GiftedChat, Time, Send, InputToolbar } from 'react-native-gifted-chat';
import { Input, Button, Icon } from 'react-native-elements'
import {colors} from '../../../assets'
import { AppointmentCard } from '../../../components/Appointment/Card';

export interface IProps {
  navigation: any;
}
export interface IState {
  messages: any[];
}

class AppointmentMessageScreen extends React.Component<IProps, IState> {
  state = {
    messages: []
  }
  componentDidMount() {
    this.setState({
      messages: [
        {
          _id: 1,
          text: 'Hello developer',
          createdAt: new Date(),
          user: {
            _id: 2,
            name: 'React Native',
            avatar: 'https://placeimg.com/140/140/any',
          },
        },
      ],
    })
  }

  onSend(messages = []) {
    this.setState(previousState => ({
      messages: GiftedChat.append(previousState.messages, messages),
    }))
  }

  renderBubble = (props: any) => {
    return  (
      <Bubble
        {...props}
        textStyle={{
          left: styles.bubbleText, 
          right: styles.bubbleText, 
        }}
        wrapperStyle={{
          left: styles.bubbleContainer,
          right: styles.bubbleContainer,
        }}
      />
  )}

  renderTime = (props: any) => {
    return  (
      <Time
        {...props}
        timeTextStyle={{
          right: styles.timeText,           
          left: styles.timeText   
        }}
      />
  )}
  
  renderSend = (props) => {
    return (
      <Send
        {...props}
        containerStyle={styles.sendContainer}
      >
        <Icon name='send' type='material-community-icon' size={30} color={colors.ocean1} />
      </Send>
    );
  }

  render() {
    return (
      <View style={{ flex: 1 }}> 
        <StatusBar barStyle="dark-content" backgroundColor={colors.ocean1}  />
        <SafeAreaView style={{ flex: 1, backgroundColor: colors.ocean5 }}>
          <GiftedChat
            alwaysShowSend={true}
            renderBubble={this.renderBubble}
            renderTime={this.renderTime}
            renderSend={this.renderSend}
            messages={this.state.messages}
            onSend={(messages: any) => this.onSend(messages)}
            user={{
              _id: 1,
            }}
          />        
        </SafeAreaView>
      </View>
    );
  } 
};
export default AppointmentMessageScreen;
const deviceWidth = Dimensions.get('window').width;
const deviceHeight = Dimensions.get('window').height;
const styles = StyleSheet.create({
  sendContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center',
    marginRight: 15,
  },
  timeText: {
    color: '#adadad',
    fontFamily: 'Gill Sans',
    fontSize: 12,
  },
  bubbleText: {
    color: colors.black,
    fontFamily: 'Gill Sans',
  },
  bubbleContainer: {
    backgroundColor: '#ececec', 
  },
});

