# frozen_string_literal: true

Rails.application.routes.draw do
  root to: 'navs#home'

  devise_for :users, controllers: {
    sessions: 'users/sessions',
    registrations: 'users/registrations'
  }

  resources :users
  resources :events

  get 'all_tags' => 'tags#all_tags', as: 'all_tags'
end
