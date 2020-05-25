# frozen_string_literal: true

Rails.application.routes.draw do
  root to: 'navs#home'

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations',
  }

  devise_scope :user do
    get 'sign_out', to: 'devise/sessions#destroy'
  end


  get '/join_event/:id', to: 'events#join', as: 'join_event'
  get '/quit_event/:id', to: 'events#quit', as: 'quit_event'
  resources :users
  resources :events

  get 'all_tags' => 'tags#all_tags', as: 'all_tags'
end
